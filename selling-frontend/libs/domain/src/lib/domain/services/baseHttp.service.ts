import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export abstract class BaseHttpService<T> {
    protected readonly BASE_URL = 'http://localhost:8090/api';

    constructor(protected readonly http: HttpClient) {}

    abstract get entityName(): string;

    protected get basePath(): string {
        return this.BASE_URL + '/' + this.entityName;
    }

    getAll(): Observable<T[]> {
        return this.http.get<T[]>(this.basePath);
    }

    getById(id: number): Observable<T> {
        return this.http.get<T>(`${this.basePath}/${id}`);
    }

    create(entity: T): Observable<number> {
        return this.http.post<number>(this.basePath, entity);
    }

    update(id: number, entity: T): Observable<number> {
        return this.http.put<number>(`${this.basePath}/${id}`, entity);
    }

    delete(id: number): Observable<number> {
        return this.http.delete<number>(`${this.basePath}/${id}`);
    }
}