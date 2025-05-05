import { Task } from "./task.model";

export interface TodoList {
    id: number;
    title: string;
    archived: boolean;
    tasks: Task[];
}