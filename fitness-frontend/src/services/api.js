import axios from "axios"

const API_URL = 'http://localhost:8080/api'

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    }

})

api.interceptors.request.use((config) => {
    const userID = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if ( token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }

    if ( userID) {
        config.headers['X-User-ID'] = userID;
    }
    return config;
})

export const getActivities =() => api.get('/activities');

export const addActivity = (activity) => api.post('/activities/setactivities', activity);

export const getActivityDetail = (id) => api.get(`/recommendation/getUserRecommendation/${id}`);
export const getAIRecommendationsforUser = (id) => api.get(`/recommendation/getUserRecommendation/${id}`);



export default api;