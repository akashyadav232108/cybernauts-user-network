import React, { memo } from 'react';
import { Handle, Position, NodeProps } from 'reactflow';
import './HighScoreNode.css';

interface NodeData {
  username: string;
  age: number;
  popularityScore: number;
  onNodeClick: (id: string) => void;
  onHobbyDrop?: (nodeId: string, hobby: string) => void;
}

const HighScoreNode = ({ data, id }: NodeProps<NodeData>) => {
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
      className="high-score-node"
      onClick={() => data.onNodeClick(id)}
      onDrop={handleDrop}
      onDragOver={handleDragOver}
    >
      <Handle type="target" position={Position.Top} />
      <div className="high-score-node-username">{data.username}</div>
      <div className="high-score-node-age">Age: {data.age}</div>
      <div className="high-score-node-score">
        ⭐ Score: {data.popularityScore.toFixed(1)}
      </div>
      <Handle type="source" position={Position.Bottom} />
    </div>
  );
};

export default memo(HighScoreNode);

