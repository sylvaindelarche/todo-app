import { Component, inject, Input } from '@angular/core';
import { Task } from '../../shared/models/task.model';
import { TodoListService } from '../../shared/data-access/todo-list.service';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-task',
  imports: [FormsModule, DialogModule, ButtonModule],
  templateUrl: './task.component.html',
  styleUrl: './task.component.css'
})
export class TaskComponent {
  private readonly todoListService = inject(TodoListService);

  @Input() task: Task | null = null;
  @Input() listId: number = 0;

  taskName = '';
  visible = false;

  onDeleteTask(listId: number, taskId: number): void {
    this.todoListService.deleteTask(listId, taskId);
  }

  onCompleteTask(listId: number, taskId: number): void {
    this.todoListService.completeTask(listId, taskId);
  }

  onShowDialog(taskName: string): void {
    this.taskName = taskName;
    this.visible = true;
  }

  onSaveDialog(listId: number, taskId: number): void {
    this.todoListService.updateTask(listId, taskId, this.taskName);
    this.visible = false;
  }

  onCancelDialog(): void {
    this.taskName = '';
    this.visible = false;
  }
}
