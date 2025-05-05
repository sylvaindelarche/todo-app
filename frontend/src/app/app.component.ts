import { Component } from '@angular/core';
import { ListsComponent } from "./features/lists/lists.component";

@Component({
  selector: 'app-root',
  imports: [ListsComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
