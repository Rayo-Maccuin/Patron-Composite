import { useState, useEffect } from 'react';
import TaskForm from './components/TaskForm';
import TaskTree from './components/TaskTree';
import { getTasks } from './api/taskService';
import './App.css';

export default function App() {
  const [tasks, setTasks] = useState([]);

  const fetchTasks = async () => {
    try {
      const data = await getTasks();
      setTasks(data);
    } catch (err) {
      console.error('Error cargando tareas:', err);
    }
  };

  useEffect(() => { fetchTasks(); }, []);

  return (
    <div className="container">
      <header>
        <h1>📋 Task Composite</h1>
        <p>Patrón Composite — Spring Boot + React</p>
      </header>
      <TaskForm tasks={tasks} onCreated={fetchTasks} />
      <TaskTree tasks={tasks} onChanged={fetchTasks} />
    </div>
  );
}