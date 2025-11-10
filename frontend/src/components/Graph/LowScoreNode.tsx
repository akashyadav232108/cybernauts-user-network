import { memo } from 'react';
import { Handle, Position, NodeProps } from 'reactflow';
import './LowScoreNode.css';

interface NodeData {
  username: string;
  age: number;
  popularityScore: number;
  onNodeClick: (id: string) => void;
  onHobbyDrop?: (nodeId: string, hobby: string) => void;
}

const LowScoreNode = ({ data, id }: NodeProps<NodeData>) => {
  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    const hobby = e.dataTransfer.getData('hobby');
    if (hobby && data.onHobbyDrop) {
      data.onHobbyDrop(id, hobby);
    }
  };

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
  };

  return (
    <div
      className="low-score-node"
      onClick={() => data.onNodeClick(id)}
      onDrop={handleDrop}
      onDragOver={handleDragOver}
    >
      <Handle type="target" position={Position.Top} />
      <div className="low-score-node-username">{data.username}</div>
      <div className="low-score-node-age">Age: {data.age}</div>
      <div className="low-score-node-score">
        Score: {data.popularityScore.toFixed(1)}
      </div>
      <Handle type="source" position={Position.Bottom} />
    </div>
  );
};

export default memo(LowScoreNode);

