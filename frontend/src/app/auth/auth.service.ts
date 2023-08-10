import { HttpClient } from "@angular/common/http"
import { Observable, map, of } from "rxjs";
import { Injectable } from '@angular/core';
import { User } from "src/app/user/user.model";
import { enviorment } from "../enviorment/enviorment";

@Injectable()
export class AuthService {
    http:HttpClient
    constructor(http:HttpClient){
        this.http=http;
    }

    logIn(username:string, password:string):Observable<any>{
        return this.http.post(`${enviorment.backendHost}/auth/login`, {username:username, password:password}, {responseType: 'text'})
    }

    getLoggedInUser(){
        return this.http.get<User>(`${enviorment.backendHost}/user/logged-in`);
    }

    getPfp(id:number,file:string){
        return this.http.get(`${enviorment.backendHost}/img/pfp/${id}/${file}`, { responseType: 'blob' } )
    }

    register(username:string, displayName:string, password:string){
        return this.http.post<{username:string,displayName:string,password:string,role:string,active:boolean}>(`${enviorment.backendHost}/auth/register`,{username:username,displayName:displayName,password:password,role:"USER",active:true})
    }
    
    
}