import { Component, inject, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TodoListService } from '../../shared/data-access/todo-list.service';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-list',
  imports: [FormsModule, InputTextModule],
  templateUrl: './list.component.html',
  styleUrl: './list.component.css'
})
export class ListComponent {
  private readonly todoListService = inject(TodoListService);
  @Input() listId = 0;
  newTaskName = '';

  onAddTask(listIdParam: number): void {
    this.todoListService.addTask(listIdParam, this.newTaskName);
  }
}
