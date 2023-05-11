import { User } from "./user";

export class Department {
    id!: number;
    name!: string;
    head!: User;
    description!: string;
    directorate!:string;
    employees!: User[];

    shown!: boolean;
    depForm!: boolean;
    editForm!: boolean;
    deleteConfirm!: boolean;
}
