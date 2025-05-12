import { Component } from '@angular/core';
import { ListsComponent } from "./features/lists/lists.component";
import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-root',
  imports: [ListsComponent, MenubarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  items: MenuItem[] = [];
}
