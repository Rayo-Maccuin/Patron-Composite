import { useState } from 'react';
import { createTask } from '../api/taskService';

export default function TaskForm({ tasks, onCreated }) {
  const [form, setForm] = useState({
    title:    '',
    type:     'TASK',
    progress: 0,
    parentId: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError]     = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await createTask({
        title:    form.title,
        type:     form.type,
        progress: form.type === 'TASK' ? parseInt(form.progress) : 0,
        parentId: form.parentId === '' ? null : parseInt(form.parentId),
      });
      setForm({ title: '', type: 'TASK', progress: 0, parentId: '' });
      onCreated();
    } catch (err) {
      setError('Error al crear la tarea. Verifica que el backend esté corriendo.');
    } finally {
      setLoading(false);
    }
  };

  // Solo muestra grupos como posibles padres
  const groups = flattenTasks(tasks).filter(t => t.type === 'GROUP');

  return (
    <form onSubmit={handleSubmit} className="form-card">
      <h2>Nueva Tarea</h2>

      <div className="form-group">
        <label>Título</label>
        <input
          name="title"
          value={form.title}
          onChange={handleChange}
          placeholder="Nombre de la tarea o grupo"
          required
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Tipo</label>
          <select name="type" value={form.type} onChange={handleChange}>
            <option value="TASK">Tarea</option>
            <option value="GROUP">Grupo</option>
          </select>
        </div>

        <div className="form-group">
          <label>Grupo padre</label>
          <select name="parentId" value={form.parentId} onChange={handleChange}>
            <option value="">Sin padre (raíz)</option>
            {groups.map(g => (
              <option key={g.id} value={g.id}>{g.title}</option>
            ))}
          </select>
        </div>
      </div>

      {form.type === 'TASK' && (
        <div className="form-group">
          <label>Progreso: {form.progress}%</label>
          <input
            type="range"
            name="progress"
            min="0"
            max="100"
            value={form.progress}
            onChange={handleChange}
          />
        </div>
      )}

      {error && <p className="error">{error}</p>}

      <button type="submit" disabled={loading}>
        {loading ? 'Creando...' : '+ Crear'}
      </button>
    </form>
  );
}

// Aplana el árbol para obtener todos los nodos
function flattenTasks(tasks) {
  const result = [];
  const traverse = (nodes) => {
    nodes.forEach(node => {
      result.push(node);
      if (node.children?.length) traverse(node.children);
    });
  };
  traverse(tasks);
  return result;
}