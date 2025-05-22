import { TestBed } from '@angular/core/testing';

import { TodoListService } from './todo-list.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('ListsService', () => {
  let service: TodoListService;

  beforeEach(() => {
    TestBed.configureTestingModule({      
      providers: [
        provideHttpClient(
          withInterceptorsFromDi(),
        ),
      ]
    });
    service = TestBed.inject(TodoListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
