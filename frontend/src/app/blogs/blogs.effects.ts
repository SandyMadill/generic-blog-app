import { Injectable } from "@angular/core"
import { Actions, createEffect, ofType } from "@ngrx/effects"
import { createAction } from "@ngrx/store"
import { BlogsService } from "./blogs.service"
import { getBlogByIdFailed, getBlogByIdRequested, getBlogsByIdSucceeded, getBlogsFailed, getBlogsForUserFailed, getBlogsForUserRequested, getBlogsForUserSucceeded, getBlogsRequested, getBlogsSucceeded, postBlogFailed, postBlogRequested, postBlogSucceeded, postThumbnailFailed, postThumbnailRequested, postThumbnailSucceeded } from "./blogs.actions"
import { catchError, map, of, switchMap, tap } from "rxjs"
import { Router } from "@angular/router"

@Injectable()
export class BlogsEffects{

    getBlogs$ = createEffect(() =>
        this.actions$.pipe(
            ofType(getBlogsRequested),
            switchMap(action => this.service.getBlogsRequest(action.payload)
                .pipe(
                    map(payload => (getBlogsSucceeded({payload:payload}))),
                    catchError(e => of(getBlogsFailed({error:e})))
                )
            )
        )
    )

    getBlogsForUser$ = createEffect(() =>
        this.actions$.pipe(
            ofType(getBlogsForUserRequested),
            switchMap(action => this.service.getBlogsForUser(action.payload)
                .pipe(
                    map(payload => (getBlogsForUserSucceeded({payload:payload}))),
                    catchError(e => of(getBlogsForUserFailed({error:e})))
                )
            )
        )
    )

    getBlogById$ = createEffect(() =>
        this.actions$.pipe(
            ofType(getBlogByIdRequested),
            switchMap(action => this.service.getBlogById(action.payload)
                .pipe(
                    map(payload => (getBlogsByIdSucceeded({payload:payload}))),
                    catchError(e => of(getBlogByIdFailed({error:e})))
                )
            )
        )
    )

    postBlog$ = createEffect(() =>
        this.actions$.pipe(
            ofType(postBlogRequested),
            switchMap(action => this.service.uploadBlogPost(action.payload.title,action.payload.blogText)
                .pipe(
                    map(payload => (postBlogSucceeded({payload:payload}))),
                    catchError(e => of(postBlogFailed({error:e})))
                )
            )
        )
    )

    postThumbnail$ = createEffect(() =>
        this.actions$.pipe(
            ofType(postThumbnailRequested),
            switchMap(action => this.service.uploadThumbnail(action.payload.file,action.payload.id)
                .pipe(
                    map(() => (postThumbnailSucceeded())),
                    catchError(e => of(postThumbnailFailed({error:e})))
                )
            )
        )
    )

    constructor(
        private readonly actions$:Actions,
        private readonly service:BlogsService,
        private readonly router:Router
    ){}
}