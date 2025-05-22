import { Component, effect, inject, OnInit } from '@angular/core';
import { TodoListService } from '../../shared/data-access/todo-list.service';
import { FormsModule } from '@angular/forms';
import { ListComponent } from '../list/list.component';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-lists',
  imports: [FormsModule, ListComponent, InputTextModule, DialogModule, ButtonModule],
  templateUrl: './lists.component.html',
  styleUrl: './lists.component.css'
})
export class ListsComponent implements OnInit {
  private readonly todoListService = inject(TodoListService);

  public readonly lists = this.todoListService.todoLists;

  listTitle = '';
  visible = false;

  ngOnInit(): void {
      this.todoListService.getLists().subscribe();
  }

  onShowDialog(): void {
    this.listTitle = '';
    this.visible = true;
  }

  onSaveDialog(): void {
    this.todoListService.addList(this.listTitle).subscribe();
    this.visible = false;
  }

  onCancelDialog(): void {
    this.listTitle = '';
    this.visible = false;
  }
}
