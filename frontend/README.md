# User Relationship & Hobby Network - Frontend

React + TypeScript frontend for the Interactive User Relationship & Hobby Network application.

## Prerequisites

- Node.js (v18+)
- npm or yarn
- Backend API running on `http://localhost:8080`

## Setup Instructions

### 1. Install Dependencies

```bash
npm install
```

### 2. Configure Environment Variables

Create a `.env` file in the frontend root:

```bash
VITE_API_BASE_URL=http://localhost:8080
```

### 3. Run Development Server

```bash
npm run dev
```

The app will be available at: `http://localhost:5173/`

### 4. Build for Production

```bash
npm run build
```

## How to Use the Application

### **Create a User**
1. Click **"+ Create User"** button (top-right)
2. Fill in username, age, and add hobbies
3. Click **"Create"** → User appears as a node in the graph

### **Edit a User**
1. Click on any user node
2. Modify details in the form that appears
3. Click **"Update"** to save changes

### **Delete a User**
1. Click on the user node to open the form
2. Click **"Delete User"** button
3. If user has friendships, unlink them first
4. Confirm deletion

### **Create a Friendship (Link Users)**
1. Click and hold the **bottom circle** of a user node
2. Drag to the **top circle** of another user node
3. Release → Friendship created, scores update

### **Remove a Friendship (Unlink Users)**
1. **Hover** over the line connecting two users (it will highlight)
2. **Click** on the line
3. Confirm in the dialog → Friendship removed, scores update

### **Add Hobbies to Users**
1. Drag a hobby from the **left sidebar**
2. Drop it onto any user node
3. Popularity score recalculates automatically

### **Search/Filter Hobbies**
- Use the search box in the left sidebar to find specific hobbies

---

## Features

✅ **React Flow Graph Visualization**
- Custom node types based on popularity score
- Drag nodes to connect users (create friendships)
- Dynamic node colors and sizes

✅ **Drag & Drop Hobbies**
- Drag hobbies from sidebar onto user nodes
- Automatically updates popularity score
- Real-time graph updates

✅ **User Management**
- Create new users with form validation
- Edit existing users (click on node)
- Delete users (with friendship unlinking)

✅ **State Management**
- Redux Toolkit for global state
- Async operations with thunks
- Real-time sync with backend

✅ **UI/UX**
- Toast notifications for all actions
- Loading states during API calls
- Error boundary for crash protection
- Responsive design

## Project Structure

```
src/
├── components/
│   ├── Graph/           # React Flow nodes and graph
│   ├── Sidebar/         # Hobbies sidebar
│   ├── UserForm/        # User create/edit form
│   └── Loader/          # Loading spinner
├── redux/               # Redux store and slices
├── services/            # API service layer
├── types.ts             # TypeScript interfaces
└── App.tsx              # Main app component
```

## Tech Stack

- **React 19** - UI library
- **TypeScript** - Type safety
- **Redux Toolkit** - State management
- **React Flow** - Graph visualization
- **React Hook Form** - Form handling
- **Yup** - Validation
- **Axios** - HTTP client
- **React Hot Toast** - Notifications
- **Vite** - Build tool

## API Integration

The frontend connects to these backend endpoints:

- `GET /api/users` - Fetch all users
- `POST /api/users` - Create user
- `PUT /api/users/:id` - Update user
- `DELETE /api/users/:id` - Delete user
- `POST /api/users/:id/link` - Link friendship
- `DELETE /api/users/:id/unlink` - Unlink friendship
- `GET /api/users/graph` - Get graph data

## Development

### Available Scripts

- `npm run dev` - Start dev server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

### Environment Variables

- `VITE_API_BASE_URL` - Backend API URL (default: http://localhost:8080)

## Troubleshooting

**Backend connection failed:**
- Ensure backend is running on port 8080
- Check CORS configuration in backend
- Verify `.env` file exists with correct API URL

**Nodes not updating:**
- Check Redux DevTools for state changes
- Verify API responses in Network tab
- Ensure backend returns correct graph data format

**Drag & drop not working:**
- Check browser console for errors
- Verify hobby data is being passed correctly
- Test with Chrome/Edge (best compatibility)

