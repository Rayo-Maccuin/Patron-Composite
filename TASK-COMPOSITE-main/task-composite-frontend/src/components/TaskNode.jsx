import { useState } from 'react';
import { updateProgress, deleteTask } from '../api/taskService';

export default function TaskNode({ task, depth = 0, onChanged }) {
  const [editing, setEditing]   = useState(false);
  const [progress, setProgress] = useState(task.progress);
  const isGroup = task.type === 'GROUP';

  const handleProgressSave = async () => {
    await updateProgress(task.id, progress);
    setEditing(false);
    onChanged();
  };

  const handleDelete = async () => {
    if (!confirm(`¿Eliminar "${task.title}"?`)) return;
    await deleteTask(task.id);
    onChanged();
  };

  return (
    <div className="task-node" style={{ marginLeft: depth * 24 + 'px' }}>
      <div className={`task-row ${isGroup ? 'is-group' : 'is-task'}`}>

        {/* Ícono tipo */}
        <span className="task-icon">{isGroup ? '📁' : '✅'}</span>

        {/* Título */}
        <span className="task-title">{task.title}</span>

        {/* Barra de progreso */}
        <div className="progress-bar-wrap">
          <div
            className="progress-bar-fill"
            style={{ width: task.progress + '%' }}
          />
        </div>
        <span className="progress-label">{task.progress}%</span>

        {/* Acciones */}
        {!isGroup && (
          <button className="btn-edit" onClick={() => setEditing(!editing)}>
            ✏️
          </button>
        )}
        <button className="btn-delete" onClick={handleDelete}>🗑️</button>
      </div>

      {/* Editor de progreso inline */}
      {editing && (
        <div className="progress-editor" style={{ marginLeft: depth * 24 + 24 + 'px' }}>
          <input
            type="range"
            min="0"
            max="100"
            value={progress}
            onChange={e => setProgress(parseInt(e.target.value))}
          />
          <span>{progress}%</span>
          <button onClick={handleProgressSave}>Guardar</button>
          <button onClick={() => setEditing(false)}>Cancelar</button>
        </div>
      )}

      {/* Hijos — recursión del Composite */}
      {task.children?.map(child => (
        <TaskNode
          key={child.id}
          task={child}
          depth={depth + 1}
          onChanged={onChanged}
        />
      ))}
    </div>
  );
}