import axios from 'axios';
import { User, GraphData, UserFormData } from '../types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// API endpoints
export const userApi = {
  // GET /api/users - Fetch all users
  getAllUsers: async (): Promise<User[]> => {
    const response = await api.get('/api/users');
    return response.data;
  },

  // GET /api/users/:id - Fetch single user
  getUserById: async (id: string): Promise<User> => {
    const response = await api.get(`/api/users/${id}`);
    return response.data;
  },

  // POST /api/users - Create new user
  createUser: async (userData: UserFormData): Promise<User> => {
    const response = await api.post('/api/users', userData);
    return response.data;
  },

  // PUT /api/users/:id - Update user
  updateUser: async (id: string, userData: UserFormData): Promise<User> => {
    const response = await api.put(`/api/users/${id}`, userData);
    return response.data;
  },

  // DELETE /api/users/:id - Delete user
  deleteUser: async (id: string): Promise<void> => {
    await api.delete(`/api/users/${id}`);
  },

  // POST /api/users/:id/link - Create friendship
  linkUsers: async (userId: string, friendId: string): Promise<void> => {
    await api.post(`/api/users/${userId}/link?friendId=${friendId}`);
  },

  // DELETE /api/users/:id/unlink - Remove friendship
  unlinkUsers: async (userId: string, friendId: string): Promise<void> => {
    await api.delete(`/api/users/${userId}/unlink?friendId=${friendId}`);
  },

  // GET /api/users/graph - Get graph data
  getGraphData: async (): Promise<GraphData> => {
    const response = await api.get('/api/users/graph');
    return response.data;
  },
};

export default api;

