import { Component, inject, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TodoListService } from '../../shared/data-access/todo-list.service';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { TodoList } from '../../shared/models/todo-list.model';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { TaskComponent } from '../task/task.component';

@Component({
  selector: 'app-list',
  imports: [FormsModule, InputTextModule, CardModule, DialogModule, ButtonModule, TaskComponent],
  templateUrl: './list.component.html',
  styleUrl: './list.component.css'
})
export class ListComponent {
  private readonly todoListService = inject(TodoListService);

  @Input() list: TodoList | null = null;

  taskName = '';
  listTitle = '';
  createTaskVisible = false;
  editListVisible = false;

  onAddTask(listIdParam: number): void {
    this.todoListService.addTask(listIdParam, this.taskName);
  }

  onDeleteList(id: number): void {
    this.todoListService.deleteList(id).subscribe();
  }

  onArchiveList(id: number): void {
    this.todoListService.archiveList(id).subscribe();
  }

  onShowCreateDialog(): void {
    this.taskName = '';
    this.createTaskVisible = true;
  }

  onSaveCreateDialog(listId: number): void {
    this.todoListService.addTask(listId, this.taskName).subscribe();
    this.createTaskVisible = false;
  }

  onCancelCreateDialog(): void {
    this.taskName = '';
    this.createTaskVisible = false;
  }

  onShowEditDialog(listTitle: string): void {
    this.listTitle = listTitle;
    this.editListVisible = true;
  }

  onSaveEditDialog(listId: number): void {
    this.todoListService.updateList(listId, this.listTitle).subscribe();
    this.editListVisible = false;
  }

  onCancelEditDialog(): void {
    this.listTitle = '';
    this.editListVisible = false;
  }
}
