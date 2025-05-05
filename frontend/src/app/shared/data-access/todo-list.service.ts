import { effect, Injectable, signal } from '@angular/core';
import { TodoList } from '../models/todo-list.model';
import { Task } from '../models/task.model';

const TODO_LISTS: TodoList[] = [
  { id: 1, title: 'List 1', archived: false, tasks: [{id: 1, name: 'Task 1', completed: false}, {id: 2, name: 'Task 2', completed: false}] },
  { id: 2, title: 'List 2', archived: false, tasks: [{id: 3, name: 'Task 3', completed: false}] },
  { id: 3, title: 'List 3', archived: false, tasks: [{id: 4, name: 'Task 4', completed: false}, {id: 5, name: 'Task 5', completed: false}, {id: 6, name: 'Task 6', completed: false}] }
]; // TODO temporary mocking until service handles real backend communication

@Injectable({
  providedIn: 'root'
})
export class TodoListService {

  constructor() { //TODO remove
    effect(() => console.log("update"));
  }

  private readonly _todoLists = signal<TodoList[]>(TODO_LISTS);
  public readonly todoLists = this._todoLists.asReadonly();

  private readonly _nextTaskId = signal<number>(6); // TODO temporary fixture until backend is in place

  public getLists(): void {
    this._todoLists.update(lists => lists);
  }

  public addList(title: string): void {
    const newList = {
      id: this._todoLists().length + 1,
      title,
      archived: false,
      tasks: []
    };
    this._todoLists.update(lists => [...lists, newList]);
  }

  public deleteList(id: number): void {
    this._todoLists.update(lists => lists.filter(list => list.id !== id));
  }

  public updateList(id: number, title: string): void { // TODO currently unused until real backend communication is handled
    this._todoLists.update(lists => lists.map(list => 
      list.id === id ? {...list, title: title} : list));
  }

  public archiveList(id: number): void {
    this._todoLists.update(lists => lists.map(list => 
      list.id === id ? {...list, archived: list.archived ? false : true} : list));
  }

  public addTask(listId: number, name: string): void {
    this._todoLists.update(lists => lists.map(list =>
      list.id === listId ? {...list, tasks: [...list.tasks, { id: this._nextTaskId(), name: name, completed: false }]} : list));
    this._nextTaskId.update(num => num + 1);
  }

  public updateTask(listId: number, taskId: number, name: string): void { // TODO currently unused until real backend communication is handled
    this._todoLists.update(lists => lists.map(list =>
      list.id === listId ? {...list, tasks: list.tasks.map(task => 
        task.id === taskId ? {...task, name: name} : task
      )} : list));
  }

  public deleteTask(listId: number, taskId: number): void {
    this._todoLists.update(lists => lists.map(list =>
      list.id === listId ? {...list, tasks: list.tasks.filter(list => list.id !== taskId)}: list
    ));
  }

  public completeTask(listId: number, taskId: number): void {
    this._todoLists.update(lists => lists.map(list =>
      list.id === listId ? {...list, tasks: list.tasks.map(task => 
        task.id === taskId ? {...task, completed: task.completed ? false : true} : task
      )} : list));
  }
}
