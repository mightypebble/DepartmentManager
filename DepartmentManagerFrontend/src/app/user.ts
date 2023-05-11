export class User {
    id!: number;
    name!: string;
    surname!: string;
    age!: string;
    ucn!: string;
    position!: string;
    department!: string;
    directorate!: string;
    username!: string;
    password!: string;
    blocked!: boolean;
    banExpirationDate!: Date;

    shown!: boolean
    userForm!: boolean;
    editForm!: boolean;
    deleteConfirm!: boolean;
}
