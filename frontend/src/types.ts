// User types matching backend DTO
export interface User {
  id: string;
  username: string;
  age: number;
  hobbies: string[];
  popularityScore: number;
  friendIds: string[];
}

// Graph data from backend
export interface GraphNode {
  id: string;
  username: string;
  age: number;
  popularityScore: number;
}

export interface GraphEdge {
  source: string;
  target: string;
}

export interface GraphData {
  nodes: GraphNode[];
  edges: GraphEdge[];
}

// Form data for creating/updating users
export interface UserFormData {
  username: string;
  age: number;
  hobbies: string[];
}

