import axios from "axios";

const API_URL = "http://localhost:8080/api";

const api = axios.create({
  baseURL: API_URL,
});

/* -------- Interceptor -------- */
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    if (userId) {
      config.headers["X-User-ID"] = userId;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

/* -------- Activity APIs -------- */

// ✅ REQUIRED by ActivityForm.jsx
export const addActivity = (activity) => {
  const userId = localStorage.getItem("userId");

  return api.post("/activities/activity", {
    ...activity,
    userId,
  });
};

export const getActivities = () =>
  api.get("/activities");

export const getActivityById = (id) =>
  api.get(`/activities/${id}`);

/* -------- Recommendation APIs -------- */

export const getActivityRecommendation = (id) =>
  api.get(`/recommendations/activity/${id}`);

// default export (rarely used directly)
export default api;
