import TaskNode from './TaskNode';

export default function TaskTree({ tasks, onChanged }) {
  if (tasks.length === 0) {
    return <p className="empty">No hay tareas aún. ¡Crea la primera!</p>;
  }

  return (
    <div className="tree-card">
      <h2>Árbol de Tareas</h2>
      {tasks.map(task => (
        <TaskNode
          key={task.id}
          task={task}
          depth={0}
          onChanged={onChanged}
        />
      ))}
    </div>
  );
}