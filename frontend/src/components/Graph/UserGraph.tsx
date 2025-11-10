import { useEffect, useCallback } from 'react';
import ReactFlow, {
  Node,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  Connection,
  BackgroundVariant,
  EdgeMouseHandler,
} from 'reactflow';
import 'reactflow/dist/style.css';
import './UserGraph.css';
import toast from 'react-hot-toast';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchGraphData, linkUsers, unlinkUsers, setSelectedUser, updateUser } from '../../redux/userSlice';
import HighScoreNode from './HighScoreNode';
import LowScoreNode from './LowScoreNode';

// Define nodeTypes outside component to prevent recreation on every render
const nodeTypes = {
  highScore: HighScoreNode,
  lowScore: LowScoreNode,
};

const UserGraph = () => {
  const dispatch = useAppDispatch();
  const { graphData, loading, users } = useAppSelector((state) => state.users);
  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);

  // Fetch graph data on mount
  useEffect(() => {
    dispatch(fetchGraphData());
  }, [dispatch]);

  // Handle node click to edit user
  const handleNodeClick = useCallback(
    (nodeId: string) => {
      dispatch(setSelectedUser(nodeId));
    },
    [dispatch]
  );

  // Handle hobby drop on node
  const handleHobbyDrop = useCallback(
    async (nodeId: string, hobby: string) => {
      const user = users.find((u) => u.id === nodeId);
      if (!user) return;

      // Check if user already has this hobby
      if (user.hobbies.includes(hobby)) {
        toast.error('User already has this hobby!');
        return;
      }

      try {
        await dispatch(
          updateUser({
            id: nodeId,
            userData: {
              username: user.username,
              age: user.age,
              hobbies: [...user.hobbies, hobby],
            },
          })
        ).unwrap();

        toast.success(`Added "${hobby}" to ${user.username}!`);
        
        // Refresh graph to show updated popularity score
        dispatch(fetchGraphData());
      } catch (error: any) {
        toast.error(error.message || 'Failed to add hobby');
      }
    },
    [dispatch, users]
  );

  // Transform backend graph data to React Flow format
  useEffect(() => {
    if (!graphData) return;

    const flowNodes: Node[] = graphData.nodes.map((node, index) => ({
      id: node.id,
      type: node.popularityScore > 5 ? 'highScore' : 'lowScore',
      position: {
        x: (index % 4) * 250,
        y: Math.floor(index / 4) * 200,
      },
      data: {
        username: node.username,
        age: node.age,
        popularityScore: node.popularityScore,
        onNodeClick: handleNodeClick,
        onHobbyDrop: handleHobbyDrop,
      },
    }));

    // Remove duplicate edges (backend returns bidirectional)
    const uniqueEdges = new Map();
    graphData.edges.forEach((edge) => {
      const key = [edge.source, edge.target].sort().join('-');
      if (!uniqueEdges.has(key)) {
        uniqueEdges.set(key, {
          id: `e-${edge.source}-${edge.target}`,
          source: edge.source,
          target: edge.target,
          animated: true,
          style: { stroke: '#667eea', strokeWidth: 2 },
          className: 'deletable-edge',
        });
      }
    });

    setNodes(flowNodes);
    setEdges(Array.from(uniqueEdges.values()));
  }, [graphData, handleNodeClick, handleHobbyDrop, setNodes, setEdges]);

  // Handle connection between nodes (drag to connect)
  const onConnect = useCallback(
    async (connection: Connection) => {
      if (!connection.source || !connection.target) return;

      try {
        await dispatch(
          linkUsers({
            userId: connection.source,
            friendId: connection.target,
          })
        ).unwrap();

        toast.success('Users connected successfully!');
        
        // Refresh graph data
        dispatch(fetchGraphData());
      } catch (error: any) {
        toast.error(error.message || 'Failed to connect users');
      }
    },
    [dispatch]
  );

  // Handle edge click to delete connection (unlink users)
  const onEdgeClick: EdgeMouseHandler = useCallback(
    async (event, edge) => {
      event.stopPropagation();
      
      const confirmed = window.confirm(
        'Are you sure you want to remove this friendship?'
      );

      if (!confirmed) return;

      try {
        await dispatch(
          unlinkUsers({
            userId: edge.source,
            friendId: edge.target,
          })
        ).unwrap();

        toast.success('Friendship removed successfully!');
        
        // Refresh graph data
        dispatch(fetchGraphData());
      } catch (error: any) {
        toast.error(error.message || 'Failed to remove friendship');
      }
    },
    [dispatch]
  );

  if (loading) {
    return (
      <div className="user-graph-loading">
        <div>Loading graph...</div>
      </div>
    );
  }

  return (
    <div className="user-graph-container">
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        onEdgeClick={onEdgeClick}
        nodeTypes={nodeTypes}
        fitView
        attributionPosition="bottom-left"
      >
        <Controls />
        <Background variant={BackgroundVariant.Dots} gap={12} size={1} />
      </ReactFlow>
    </div>
  );
};

export default UserGraph;
