import { ChangeDetectionStrategy, Component, ElementRef, Input } from "@angular/core" 
import { DatePipe } from "@angular/common";
import { BlogsService } from "../../../blogs.service";
import { Store } from "@ngrx/store";
import { Blog } from "../../../blogs.model";
import * as blogReducer from "../../../blogs.reducer"
import { getBlogsRequested } from "../../../blogs.actions";
import { selectBlogEntites, selectFetched } from "../../../blogs.selector";
import { combineLatest } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { enviorment } from "src/app/enviorment/enviorment";
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";
import { Router } from "@angular/router";
import { CUSTOM_ELEMENTS_SCHEMA } from "@angular/compiler";

@Component({
    selector:'app-blogs-list-item',
    templateUrl:'blogs-list-item.component.html',
    styleUrls:['blogs-list-item.component.scss']
}) export class BlogsListItemComponent{

    @Input() blogId:number
    blogs$ = this.store.select(selectBlogEntites)
    blog:Blog
    private http:HttpClient
    thumbnail:SafeUrl
    desc:string
    postedAt:string
    updatedAt:string

    constructor(private readonly store:Store<blogReducer.State>, http:HttpClient, private sanitizer: DomSanitizer, private router: Router){
        this.http=http;
    }


    ngOnInit(){
        const domParser = new DOMParser();
        this.http.get(`${enviorment.backendHost}/img/thumbnail/${this.blogId}`, { responseType: 'blob' }).subscribe(pfpResponse =>{
            let objectURL = URL.createObjectURL(pfpResponse);
            this.thumbnail = this.sanitizer.bypassSecurityTrustUrl(objectURL);
          })
        this.blogs$.subscribe(blogs => {
            this.blog = blogs[this.blogId]
            if (this.blog){
                this.postedAt = this.blog.postedAt.toString()
                this.desc = domParser.parseFromString(this.blog.blogText, "text/html")
                    .body.innerText.trim()
                        .split("/n").map(l => 
                            l.trim()
                        ).join("/n").slice(0,100)
                if (this.blog.blogText.length>100){this.desc= this.desc+"..."}

                if (this.blog.updatedAt != this.blog.postedAt){
                    this.updatedAt = this.blog.updatedAt.toString()
                }
            }
        })
    }


    selectBlog(){
        this.router.navigate(["/blog/" + this.blogId])
    }
}