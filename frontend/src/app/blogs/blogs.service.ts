import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core"
import { Blog } from "./blogs.model";
import { map } from "rxjs";
import { enviorment } from "src/app/enviorment/enviorment";

@Injectable()
export class BlogsService{
    http:HttpClient

    constructor(http:HttpClient){
        this.http=http;
    }
    
    getBlogById(id:number){
        return this.http.get<any>(`${enviorment.backendHost}/blog/${id}`)
    }

    getBlogsRequest(payload:{page:number,size:number, requestType:string, search?:string}){
        let search = ""
        if(payload.search){
            search = `/${payload.search}`
        }

        return this.http.get<any>(`${enviorment.backendHost}/blog/${payload.requestType}/${payload.page}/${payload.size}${search}`).pipe(map(response=>{
            return {blogs:response.content,pages:response.totalPages}
        }))

    }

    getBlogsForUser(userId:number){
        return this.http.get<any>(`${enviorment.backendHost}/blog/user/${userId}`)
    }

    uploadBlogPost(title:string,blogText:string){
        return this.http.post<Blog>(`${enviorment.backendHost}/blog`,{title:title,blogText:blogText})
    }

    uploadThumbnail(file:File, id:number){
        const imageFormData = new FormData();
        imageFormData.append('thumbnail.png', file);
        return this.http.post(`${enviorment.backendHost}/img/thumbnail/${id}`, imageFormData)
    }
}