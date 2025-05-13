import { Component, inject, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TodoListService } from '../../shared/data-access/todo-list.service';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { TodoList } from '../../shared/models/todo-list.model';

@Component({
  selector: 'app-list',
  imports: [FormsModule, InputTextModule, CardModule],
  templateUrl: './list.component.html',
  styleUrl: './list.component.css'
})
export class ListComponent {
  private readonly todoListService = inject(TodoListService);

  @Input() list: TodoList | null = null;
  newTaskName = '';

  onAddTask(listIdParam: number): void {
    this.todoListService.addTask(listIdParam, this.newTaskName);
  }

  onDeleteList(id: number): void {
    this.todoListService.deleteList(id);
  }

  onArchiveList(id: number): void {
    this.todoListService.archiveList(id);
  }

  onDeleteTask(listId: number, taskId: number): void {
    this.todoListService.deleteTask(listId, taskId);
  }

  onCompleteTask(listId: number, taskId: number): void {
    this.todoListService.completeTask(listId, taskId);
  }
}
