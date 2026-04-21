import axios from 'axios';

const API_URL = 'http://localhost:8080/api/tasks';

export const getTasks = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

export const createTask = async (data) => {
  const response = await axios.post(API_URL, data);
  return response.data;
};

export const updateProgress = async (id, progress) => {
  const response = await axios.patch(`${API_URL}/${id}/progress`, { progress });
  return response.data;
};

export const deleteTask = async (id) => {
  await axios.delete(`${API_URL}/${id}`);
};