import React, { useEffect, useState } from 'react';
import { Provider } from 'react-redux';
import { Toaster } from 'react-hot-toast';
import './App.css';
import { store } from './redux/store';
import { useAppDispatch, useAppSelector } from './redux/hooks';
import { fetchGraphData, fetchUsers, setSelectedUser } from './redux/userSlice';
import UserGraph from './components/Graph/UserGraph';
import HobbiesSidebar from './components/Sidebar/HobbiesSidebar';
import UserForm from './components/UserForm/UserForm';

function AppContent() {
  const dispatch = useAppDispatch();
  const { users, loading } = useAppSelector((state) => state.users);
  const { selectedUserId } = useAppSelector((state) => state.users);
  const [showForm, setShowForm] = useState(false);
  const [draggedHobby, setDraggedHobby] = useState<string | null>(null);

  // Fetch initial data
  useEffect(() => {
    dispatch(fetchUsers());
    dispatch(fetchGraphData());
  }, [dispatch]);

  // Handle open form (create new user)
  const handleCreateUser = () => {
    dispatch(setSelectedUser(null));
    setShowForm(true);
  };

  // Handle close form
  const handleCloseForm = () => {
    setShowForm(false);
    dispatch(setSelectedUser(null));
  };

  // Watch for selected user changes (from node click)
  useEffect(() => {
    if (selectedUserId) {
      setShowForm(true);
    }
  }, [selectedUserId]);

  const handleHobbyDragStart = (hobby: string) => {
    setDraggedHobby(hobby);
  };

  return (
    <div className="app-container">
      {/* Header */}
      <header className="app-header">
        <h1 className="app-header-title">User Relationship & Hobby Network</h1>
        <button className="app-header-button" onClick={handleCreateUser}>
          + Create User
        </button>
      </header>

      {/* Main Content */}
      <main className="app-main">
        {/* Hobbies Sidebar */}
        <HobbiesSidebar onHobbyDragStart={handleHobbyDragStart} />

        {/* Graph Area */}
        <div className="app-graph-area">
          {loading && users.length === 0 ? (
            <div className="app-empty-state">
              <div className="app-empty-state-title">Loading...</div>
            </div>
          ) : users.length === 0 ? (
            <div className="app-empty-state">
              <div className="app-empty-state-title">No Users Yet</div>
              <div className="app-empty-state-text">
                Click "Create User" to get started!
              </div>
            </div>
          ) : (
            <UserGraph />
          )}
        </div>
      </main>

      {/* User Form Modal */}
      {showForm && <UserForm onClose={handleCloseForm} />}

      {/* Toast Notifications */}
      <Toaster
        position="top-right"
        toastOptions={{
          success: {
            duration: 3000,
            style: {
              background: '#48bb78',
              color: 'white',
            },
          },
          error: {
            duration: 4000,
            style: {
              background: '#f56565',
              color: 'white',
            },
          },
        }}
      />
    </div>
  );
}

function App() {
  return (
    <Provider store={store}>
      <AppContent />
    </Provider>
  );
}

export default App;
