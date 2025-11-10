# Deployment Guide

This guide covers deploying the full-stack application:
- **Backend (Spring Boot)** → Railway
- **Frontend (React)** → Vercel

---

## Prerequisites

- ✅ GitHub account
- ✅ Vercel account (free tier)
- ✅ Railway account (free tier with $5 credit)
- ✅ MySQL database (Railway provides free MySQL)

---

## Part 1: Deploy Backend to Railway

### Step 1: Create Railway Account
1. Go to https://railway.app/
2. Sign up with GitHub
3. You get $5 free credit (no credit card required)

### Step 2: Create New Project
1. Click **"New Project"**
2. Select **"Deploy from GitHub repo"**
3. Connect your GitHub account
4. Select your repository: `cybernauts-user-network`
5. Railway will detect it's a Maven project

### Step 3: Add MySQL Database
1. In your Railway project, click **"+ New"**
2. Select **"Database"** → **"Add MySQL"**
3. Railway automatically creates a MySQL instance

### Step 4: Configure Environment Variables
1. Click on your **backend service**
2. Go to **"Variables"** tab
3. Add these variables:

```
PORT=8080
DB_URL=${{MySQL.DATABASE_URL}}
DB_USERNAME=${{MySQL.MYSQL_USER}}
DB_PASSWORD=${{MySQL.MYSQL_PASSWORD}}
CORS_ALLOWED_ORIGINS=https://your-frontend-app.vercel.app
```

**Note:** Railway auto-fills MySQL variables. Just add PORT and CORS_ALLOWED_ORIGINS.

### Step 5: Deploy Backend
1. Railway automatically builds and deploys
2. Wait for deployment (3-5 minutes)
3. Once done, click **"Settings"** → **"Generate Domain"**
4. Copy your backend URL: `https://your-app.railway.app`

### Step 6: Test Backend
Open in browser:
```
https://your-app.railway.app/api/users
```
You should see: `[]` (empty array)

---

## Part 2: Deploy Frontend to Vercel

### Step 1: Create Vercel Account
1. Go to https://vercel.com/
2. Sign up with GitHub
3. Free tier is sufficient

### Step 2: Import Project
1. Click **"Add New..."** → **"Project"**
2. Import your GitHub repository
3. Select `cybernauts-user-network`

### Step 3: Configure Build Settings
Vercel auto-detects Vite. Verify these settings:

- **Root Directory:** `frontend`
- **Build Command:** `npm run build`
- **Output Directory:** `dist`
- **Install Command:** `npm install`

### Step 4: Add Environment Variables
In **"Environment Variables"** section, add:

```
VITE_API_BASE_URL=https://your-app.railway.app
```

**Replace with your actual Railway backend URL!**

### Step 5: Deploy Frontend
1. Click **"Deploy"**
2. Wait for build (2-3 minutes)
3. Vercel provides a URL: `https://your-app.vercel.app`

### Step 6: Update CORS in Backend
1. Go back to Railway
2. Update `CORS_ALLOWED_ORIGINS` variable:
   ```
   CORS_ALLOWED_ORIGINS=https://your-app.vercel.app
   ```
3. Railway will auto-redeploy

---

## Part 3: Test Deployed Application

### Test Checklist:
1. ✅ Open your Vercel URL
2. ✅ Click "Create User"
3. ✅ Create a user with hobbies
4. ✅ Create another user
5. ✅ Link them by dragging
6. ✅ Drag hobby onto node
7. ✅ Click edge to unlink
8. ✅ Edit user by clicking node
9. ✅ Delete user

---

## Troubleshooting

### CORS Errors
**Problem:** "CORS policy: No 'Access-Control-Allow-Origin' header"

**Solution:**
1. Check Railway environment variable `CORS_ALLOWED_ORIGINS`
2. Must match your Vercel URL exactly (including https://)
3. Redeploy backend after changing

### Backend Not Responding
**Problem:** API calls timeout or fail

**Solution:**
1. Check Railway logs: Click service → Logs tab
2. Verify database connection
3. Check environment variables are set correctly

### Frontend Shows Network Error
**Problem:** "Network Error" in browser console

**Solution:**
1. Verify `VITE_API_BASE_URL` in Vercel environment variables
2. Test backend URL directly in browser
3. Check browser console for exact error

### Database Connection Failed
**Problem:** Backend logs show "Access denied" or "Connection refused"

**Solution:**
1. Verify MySQL service is running in Railway
2. Check database credentials in environment variables
3. Use Railway's auto-generated `${{MySQL.DATABASE_URL}}`

---

## Cost Breakdown

### Railway (Backend + Database)
- **Free Tier:** $5 credit/month
- **Usage:** ~$5-7/month for hobby project
- **Includes:** Backend + MySQL database

### Vercel (Frontend)
- **Free Tier:** 100GB bandwidth/month
- **Unlimited deployments**
- **Perfect for frontend**

**Total Monthly Cost:** $0-2 (within free tiers for low traffic)

---

## Alternative: Render (Instead of Railway)

If you prefer Render for backend:

1. Go to https://render.com/
2. Create **Web Service** from GitHub
3. Build Command: `./mvnw clean package -DskipTests`
4. Start Command: `java -jar target/*.jar`
5. Add environment variables (same as Railway)
6. Render provides free PostgreSQL (use instead of MySQL)

**Note:** Render free tier sleeps after 15 min inactivity (slow first request).

---

## Monitoring & Logs

### Railway Logs
- Click service → **Logs** tab
- Real-time logs of backend

### Vercel Logs
- Go to deployment → **Logs** tab
- See build and runtime logs

---

## Update Deployment

### Backend Updates
1. Push changes to GitHub
2. Railway auto-deploys from `main` branch

### Frontend Updates
1. Push changes to GitHub
2. Vercel auto-deploys from `main` branch

Both platforms have **auto-deployment** enabled by default!

---

## Custom Domain (Optional)

### Vercel
1. Go to Project Settings → Domains
2. Add your custom domain
3. Update DNS records as instructed

### Railway
1. Go to Service Settings → Domains
2. Add custom domain
3. Update DNS CNAME record

---

## Summary

✅ Backend deployed to Railway (Java/Spring Boot support)
✅ Frontend deployed to Vercel (Best for React/Vite)
✅ MySQL database on Railway
✅ CORS configured for production
✅ Environment variables set
✅ Auto-deployment enabled

**Your app is live!** 🚀

