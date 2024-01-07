import { Injectable } from "@angular/core";
import { User } from "../models";
import { BaseHttpService } from "./baseHttp.service";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({providedIn:"root"})
export class UserService extends BaseHttpService<User>{

    constructor(readonly client: HttpClient){
        super(client);
    }

    override get entityName(): string {
        return "user";
    }

    filterAll(filter?: any): Observable<User[]> {
        return this.http.get<User[]>(this.basePath, { params: filter });
    }

    createWithUsername(entity: User): Observable<number> {
        return this.http.post<number>(this.basePath, entity);
    }

    updateWithUsername(username: string, entity: User): Observable<number> {
        return this.http.put<number>(`${this.basePath}/${username}`, entity);
    }

    deleteWithUsername(username: string): Observable<number> {
        return this.http.delete<number>(`${this.basePath}/${username}`);
    }

}