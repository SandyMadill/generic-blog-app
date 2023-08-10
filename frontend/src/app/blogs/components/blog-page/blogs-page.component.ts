import { ChangeDetectionStrategy, Component, Input } from "@angular/core" 
import { DatePipe } from "@angular/common";
import { BlogsService } from "../../blogs.service";
import { Store } from "@ngrx/store";
import { Blog } from "../../blogs.model";
import * as blogReducer from "../../blogs.reducer"
import { getBlogByIdRequested, getBlogsRequested } from "../../blogs.actions";
import { selectBlogEntites, selectFetched } from "../../blogs.selector";
import { combineLatest } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { enviorment } from "src/app/enviorment/enviorment";
import { DomSanitizer, SafeUrl, Title } from "@angular/platform-browser";
import { ActivatedRoute, Router } from "@angular/router";

@Component({
    selector:'app-blogs-page',
    templateUrl:'blogs-page.component.html',
    styleUrls:['blogs-page.component.scss']
}) export class BlogPageComponent{

    blogId:number
    blogs$ = this.store.select(selectBlogEntites)
    blog:Blog
    private http:HttpClient
    thumbnail:SafeUrl
    postedAt:string
    updatedAt:string
    fetched$ = this.store.select(selectFetched)

    constructor(private readonly store:Store<blogReducer.State>, http:HttpClient, private sanitizer: DomSanitizer, private readonly route:ActivatedRoute, private readonly title:Title){
        this.http=http;
    }


    ngOnInit(){
        combineLatest([this.blogs$,this.route.params, this.fetched$]).subscribe(([blogs, params, fetched]) => {
            if(params["id"] && blogs[params["id"]]==null){
                this.store.dispatch(getBlogByIdRequested({payload:params["id"]}))
            }
            else if((this.blog ==null && params["id"])){
                this.blog = blogs[params["id"]]
            }
            else if (this.blog !=null && params["id"]){
                this.title.setTitle(this.blog.title)
                this.blogId=params["id"]
                this.postedAt = this.blog.postedAt.toString()

                if (this.blog.updatedAt != this.blog.postedAt){
                    this.updatedAt = this.blog.updatedAt.toString()
                }

                this.http.get(`${enviorment.backendHost}/img/thumbnail/${this.blogId}`, { responseType: 'blob' }).subscribe(pfpResponse =>{
                    let objectURL = URL.createObjectURL(pfpResponse);
                     this.thumbnail = this.sanitizer.bypassSecurityTrustUrl(objectURL);
                })
            }
        })
    }
}