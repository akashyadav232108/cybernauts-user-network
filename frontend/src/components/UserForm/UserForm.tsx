import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import './UserForm.css';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import {
  createUser,
  updateUser,
  deleteUser,
  unlinkUsers,
  fetchGraphData,
} from '../../redux/userSlice';
import { UserFormData } from '../../types';

interface UserFormProps {
  onClose: () => void;
}

const UserForm = ({ onClose }: UserFormProps) => {
  const dispatch = useAppDispatch();
  const { selectedUserId, users } = useAppSelector((state) => state.users);
  const selectedUser = users.find((u) => u.id === selectedUserId);
  const isEditMode = !!selectedUser;

  const [hobbyInput, setHobbyInput] = useState('');
  const [hobbies, setHobbies] = useState<string[]>([]);

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<UserFormData>({
    defaultValues: {
      username: selectedUser?.username || '',
      age: selectedUser?.age || 18,
      hobbies: selectedUser?.hobbies || [],
    },
  });

  useEffect(() => {
    if (selectedUser) {
      reset({
        username: selectedUser.username,
        age: selectedUser.age,
        hobbies: selectedUser.hobbies,
      });
      setHobbies(selectedUser.hobbies);
    }
  }, [selectedUser, reset]);

  const onSubmit = async (data: UserFormData) => {
    try {
      const formData = { ...data, hobbies };

      if (isEditMode && selectedUserId) {
        await dispatch(updateUser({ id: selectedUserId, userData: formData })).unwrap();
        toast.success('User updated successfully!');
      } else {
        await dispatch(createUser(formData)).unwrap();
        toast.success('User created successfully!');
      }

      dispatch(fetchGraphData());
      onClose();
    } catch (error: any) {
      toast.error(error.message || 'Operation failed');
    }
  };

  const handleDelete = async () => {
    if (!selectedUserId || !selectedUser) return;

    if (selectedUser.friendIds.length > 0) {
      const confirmUnlink = window.confirm(
        'This user has friends. Do you want to unlink all friendships before deleting?'
      );
      if (!confirmUnlink) return;

      // Unlink all friends first
      try {
        for (const friendId of selectedUser.friendIds) {
          await dispatch(unlinkUsers({ userId: selectedUserId, friendId })).unwrap();
        }
      } catch (error: any) {
        toast.error('Failed to unlink friends: ' + error.message);
        return;
      }
    }

    const confirmDelete = window.confirm(
      `Are you sure you want to delete user "${selectedUser.username}"?`
    );
    if (!confirmDelete) return;

    try {
      await dispatch(deleteUser(selectedUserId)).unwrap();
      toast.success('User deleted successfully!');
      dispatch(fetchGraphData());
      onClose();
    } catch (error: any) {
      toast.error(error.message || 'Failed to delete user');
    }
  };

  const addHobby = () => {
    if (hobbyInput.trim() && !hobbies.includes(hobbyInput.trim())) {
      setHobbies([...hobbies, hobbyInput.trim()]);
      setHobbyInput('');
    }
  };

  const removeHobby = (hobby: string) => {
    setHobbies(hobbies.filter((h) => h !== hobby));
  };

  return (
    <div className="user-form-overlay" onClick={onClose}>
      <div className="user-form-modal" onClick={(e) => e.stopPropagation()}>
        <h2 className="user-form-title">
          {isEditMode ? 'Edit User' : 'Create New User'}
        </h2>

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="user-form-group">
            <label className="user-form-label">Username *</label>
            <input
              {...register('username', {
                required: 'Username is required',
                minLength: { value: 3, message: 'Minimum 3 characters' },
              })}
              className="user-form-input"
            />
            {errors.username && (
              <span className="user-form-error">{errors.username.message}</span>
            )}
          </div>

          <div className="user-form-group">
            <label className="user-form-label">Age *</label>
            <input
              type="number"
              {...register('age', {
                required: 'Age is required',
                min: { value: 1, message: 'Age must be at least 1' },
              })}
              className="user-form-input"
            />
            {errors.age && <span className="user-form-error">{errors.age.message}</span>}
          </div>

          <div className="user-form-group">
            <label className="user-form-label">Hobbies *</label>
            <div className="user-form-hobby-input-wrapper">
              <input
                value={hobbyInput}
                onChange={(e) => setHobbyInput(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), addHobby())}
                placeholder="Type a hobby and press Enter"
                className="user-form-input"
                style={{ flex: 1 }}
              />
              <button
                type="button"
                onClick={addHobby}
                className="user-form-button-add-hobby"
              >
                Add
              </button>
            </div>
            <div className="user-form-hobby-tags">
              {hobbies.map((hobby) => (
                <span key={hobby} className="user-form-hobby-tag">
                  {hobby}
                  <button
                    type="button"
                    onClick={() => removeHobby(hobby)}
                    className="user-form-hobby-remove"
                  >
                    ×
                  </button>
                </span>
              ))}
            </div>
            {hobbies.length === 0 && (
              <span className="user-form-error">At least one hobby required</span>
            )}
          </div>

          <div className="user-form-buttons">
            <button
              type="submit"
              disabled={hobbies.length === 0}
              className="user-form-button user-form-button-primary"
            >
              {isEditMode ? 'Update' : 'Create'}
            </button>
            <button
              type="button"
              onClick={onClose}
              className="user-form-button user-form-button-secondary"
            >
              Cancel
            </button>
          </div>

          {isEditMode && (
            <button
              type="button"
              onClick={handleDelete}
              className="user-form-button-danger"
            >
              Delete User
            </button>
          )}
        </form>
      </div>
    </div>
  );
};

export default UserForm;

