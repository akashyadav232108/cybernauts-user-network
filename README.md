# User Relationship & Hobby Network

A full-stack interactive application for managing user relationships and hobbies with real-time graph visualization.

## 🚀 Tech Stack

**Frontend:** React 19 + TypeScript + Redux Toolkit + React Flow + Vite  
**Backend:** Spring Boot 3 + Java 17 + JPA/Hibernate  
**Database:** MySQL  
**Deployment:** Vercel (Frontend) + Railway (Backend)

## ✨ Features

- 📊 **Interactive Graph Visualization** - React Flow powered network graph
- 👥 **User Management** - Create, edit, and delete users with form validation
- 🤝 **Friendship Links** - Drag-and-drop to create/remove connections
- 🎨 **Hobby Management** - Drag hobbies onto users to update interests
- 📈 **Popularity Scoring** - Dynamic calculation based on friendships and hobbies
- 🎯 **Node Differentiation** - Visual distinction for high/low popularity scores
- 🔄 **Real-time Updates** - Instant UI updates after every action
- 🎨 **Modern UI/UX** - Toast notifications, loading states, error handling

## 📁 Project Structure

```
cybernauts-user-network/
├── frontend/          # React + TypeScript + Vite
│   ├── src/
│   │   ├── components/
│   │   ├── redux/
│   │   ├── services/
│   │   └── types.ts
│   └── README.md      # Frontend-specific documentation
│
└── backend/           # Spring Boot + MySQL
    ├── src/main/java/com/cybernauts/backend/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── model/
    │   └── config/
    └── pom.xml
```

## 🛠️ Quick Start

### Prerequisites
- Node.js 18+
- Java 17+
- Maven 3+
- MySQL 8+

### Backend Setup
```bash
cd backend
./mvnw spring-boot:run
# Runs on http://localhost:8080
```

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
# Runs on http://localhost:5173
```

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| POST | `/api/users` | Create user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| POST | `/api/users/{id}/link` | Link friendship |
| DELETE | `/api/users/{id}/unlink` | Remove friendship |
| GET | `/api/users/graph` | Get graph data |

> 📄 For detailed API documentation, see [API.md](./API.md) (coming soon)

## 🌐 Deployment

**Frontend:** Deployed on Vercel  
**Backend:** Deployed on Railway  
**Database:** Railway MySQL

### Environment Variables
```bash
# Backend (Railway)
DB_URL=<mysql-connection-url>
DB_USERNAME=<mysql-username>
DB_PASSWORD=<mysql-password>
CORS_ALLOWED_ORIGINS=https://your-frontend.vercel.app

# Frontend (Vercel)
VITE_API_BASE_URL=https://your-backend.railway.app
```

## 🎮 How to Use

1. **Create Users** - Click "+ Create User" button
2. **Add Hobbies** - Drag hobbies from sidebar onto user nodes
3. **Link Users** - Drag from bottom circle of one node to top circle of another
4. **Remove Links** - Click on the connecting line between nodes
5. **Edit User** - Click on any user node
6. **Delete User** - Open user form and click "Delete User"

## 📸 Screenshots

- Interactive graph with draggable nodes
- Sidebar with filterable hobbies
- User creation/edit modal form
- Real-time popularity score updates

## 🧪 Testing

Run backend tests:
```bash
cd backend
./mvnw test
```

## 🤝 Contributing

This project is part of a technical assessment for Cybernauts.

## 📝 License

This project is created for educational purposes.

---

