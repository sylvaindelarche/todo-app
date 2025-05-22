import { inject, Injectable, signal } from '@angular/core';
import { TodoList } from '../models/todo-list.model';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import { Task } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TodoListService {
  private readonly http = inject(HttpClient);
  private readonly path = `http://localhost:8080/`

  private readonly _todoLists = signal<TodoList[]>([]);
  public readonly todoLists = this._todoLists.asReadonly();

  private handleApiErrors(error: Object): void {
    console.log("Error when calling API:");
    console.log(error);
  }

  public getLists(): Observable<TodoList[]> {
    return this.http.get<TodoList[]>(`${this.path}lists`).pipe(
      tap((lists) => {
        this._todoLists.set(lists);
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of([]);
      })
    );
  }

  public addList(title: string): Observable<TodoList | null> {
    const newList = {
      title: title
    };
    return this.http.post<TodoList>(`${this.path}lists`, newList).pipe(
      tap((list) => {
        this._todoLists.update(lists => [...lists, list]);
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of(null);
      })
    );
  }

  public deleteList(id: number): Observable<void> {
    return this.http.delete<void>(`${this.path}lists/${id}`).pipe(
      tap(() => {
        this._todoLists.update(lists => lists.filter(list => list.id !== id));
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of();
      })
    );
  }

  public updateList(id: number, title: string): Observable<TodoList | null> {
    const updatedList = {
      title: title
    };
    return this.http.patch<TodoList>(`${this.path}lists/${id}`, updatedList).pipe(
      tap((returnedList) => {
        this._todoLists.update(lists => lists.map(list => list.id === id ? {...list, title: returnedList.title} : list));
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of(null);
      })
    );
  }

  public archiveList(id: number): Observable<TodoList | null> {
    return this.http.post<TodoList>(`${this.path}lists/${id}/archive`, {}).pipe(
      tap((returnedList) => {
        this._todoLists.update(lists => lists.map(list => list.id === id ? {...list, archived: returnedList.archived} : list));
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of(null);
      })
    );
  }

  public addTask(listId: number, name: string): Observable<Task | null> {
    const newTask = {
      name: name
    };
    return this.http.post<Task>(`${this.path}lists/${listId}/tasks`, newTask).pipe(
      tap((task) => {
        this._todoLists.update(lists => lists.map(list => list.id === listId ? {...list, tasks: [...list.tasks, task]} : list));
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of(null);
      })
    );
  }

  public updateTask(listId: number, taskId: number, name: string): Observable<Task | null> {
    const updatedTask = {
      name: name
    };
    return this.http.patch<Task>(`${this.path}tasks/${taskId}`, updatedTask).pipe(
      tap((returnedTask) => {
        this._todoLists.update(lists => lists.map(list =>
          list.id === listId ? {...list, tasks: list.tasks.map(task => 
            task.id === taskId ? {...task, name: returnedTask.name} : task
          )} : list));
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of(null);
      })
    );
  }

  public deleteTask(listId: number, taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.path}tasks/${taskId}`).pipe(
      tap(() => {
        this._todoLists.update(lists => lists.map(list =>
          list.id === listId ? {...list, tasks: list.tasks.filter(task => task.id !== taskId)}: list
        ));
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of();
      })
    );
  }

  public completeTask(listId: number, taskId: number): Observable<Task | null> {
    return this.http.post<Task>(`${this.path}tasks/${taskId}/complete`, {}).pipe(
      tap((returnedTask) => {
        this._todoLists.update(lists => lists.map(list =>
          list.id === listId ? {...list, tasks: list.tasks.map(task => 
            task.id === taskId ? {...task, completed: returnedTask.completed} : task
          )} : list));
      }),
      catchError((error) => {
        this.handleApiErrors(error);
        return of(null);
      })
    );
  }
}
