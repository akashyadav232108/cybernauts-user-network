import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { User, GraphData, UserFormData } from '../types';
import { userApi } from '../services/api';

interface UserState {
  users: User[];
  graphData: GraphData | null;
  loading: boolean;
  error: string | null;
  selectedUserId: string | null;
}

const initialState: UserState = {
  users: [],
  graphData: null,
  loading: false,
  error: null,
  selectedUserId: null,
};

// Async thunks
export const fetchUsers = createAsyncThunk('users/fetchAll', async () => {
  return await userApi.getAllUsers();
});

export const fetchGraphData = createAsyncThunk('users/fetchGraph', async () => {
  return await userApi.getGraphData();
});

export const createUser = createAsyncThunk(
  'users/create',
  async (userData: UserFormData) => {
    return await userApi.createUser(userData);
  }
);

export const updateUser = createAsyncThunk(
  'users/update',
  async ({ id, userData }: { id: string; userData: UserFormData }) => {
    return await userApi.updateUser(id, userData);
  }
);

export const deleteUser = createAsyncThunk('users/delete', async (id: string) => {
  await userApi.deleteUser(id);
  return id;
});

export const linkUsers = createAsyncThunk(
  'users/link',
  async ({ userId, friendId }: { userId: string; friendId: string }) => {
    await userApi.linkUsers(userId, friendId);
    return { userId, friendId };
  }
);

export const unlinkUsers = createAsyncThunk(
  'users/unlink',
  async ({ userId, friendId }: { userId: string; friendId: string }) => {
    await userApi.unlinkUsers(userId, friendId);
    return { userId, friendId };
  }
);

const userSlice = createSlice({
  name: 'users',
  initialState,
  reducers: {
    setSelectedUser: (state, action) => {
      state.selectedUserId = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    // Fetch users
    builder.addCase(fetchUsers.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchUsers.fulfilled, (state, action) => {
      state.loading = false;
      state.users = action.payload;
    });
    builder.addCase(fetchUsers.rejected, (state, action) => {
      state.loading = false;
      state.error = action.error.message || 'Failed to fetch users';
    });

    // Fetch graph data
    builder.addCase(fetchGraphData.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchGraphData.fulfilled, (state, action) => {
      state.loading = false;
      state.graphData = action.payload;
    });
    builder.addCase(fetchGraphData.rejected, (state, action) => {
      state.loading = false;
      state.error = action.error.message || 'Failed to fetch graph data';
    });

    // Create user
    builder.addCase(createUser.fulfilled, (state, action) => {
      state.users.push(action.payload);
    });

    // Update user
    builder.addCase(updateUser.fulfilled, (state, action) => {
      const index = state.users.findIndex((u) => u.id === action.payload.id);
      if (index !== -1) {
        state.users[index] = action.payload;
      }
    });

    // Delete user
    builder.addCase(deleteUser.fulfilled, (state, action) => {
      state.users = state.users.filter((u) => u.id !== action.payload);
    });
  },
});

export const { setSelectedUser, clearError } = userSlice.actions;
export default userSlice.reducer;

