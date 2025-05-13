import { Component, inject, OnInit } from '@angular/core';
import { TodoListService } from '../../shared/data-access/todo-list.service';
import { FormsModule } from '@angular/forms';
import { ListComponent } from '../list/list.component';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-lists',
  imports: [FormsModule, ListComponent, InputTextModule],
  templateUrl: './lists.component.html',
  styleUrl: './lists.component.css'
})
export class ListsComponent implements OnInit {
  private readonly todoListService = inject(TodoListService);

  public readonly lists = this.todoListService.todoLists;

  listTitle = '';

  ngOnInit(): void {
      this.todoListService.getLists();
  }

  onAddList(): void {
    this.todoListService.addList(this.listTitle);
  }
}
