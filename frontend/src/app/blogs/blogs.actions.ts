import { createAction, props } from "@ngrx/store";
import { Blog } from "./blogs.model";

export const getBlogsRequested = createAction(
    "[Blogs] Get Blogs Requested",
    props<{payload:{page:number,size:number, requestType:string, search?:string}}>(),
)
export const getBlogsSucceeded = createAction(
    "[Blogs] Get Blogs Succeeded",
    props<{payload:{blogs:Blog[], pages:number}}>()
)

export const getBlogsFailed = createAction(
    "[Blogs] Get Blogs Failed",
    props<{error:Error}>()
)

export const getBlogsForUserRequested = createAction(
    "[Blogs] Get Blogs For User Requested",
    props<{payload:number}>(),
)
export const getBlogsForUserSucceeded = createAction(
    "[Blogs] Get Blogs For User Succeeded",
    props<{payload:Blog[]}>()
)

export const getBlogsForUserFailed = createAction(
    "[Blogs] Get Blogs For User Failed",
    props<{error:Error}>()
)

export const getBlogByIdRequested = createAction(
    "[Blogs] Get Blog By Id Requested",
    props<{payload:number}>(),
)
export const getBlogsByIdSucceeded = createAction(
    "[Blogs] Get Blog By Id Succeeded",
    props<{payload:Blog}>()
)

export const getBlogByIdFailed = createAction(
    "[Blogs] Get Blog By Id Failed",
    props<{error:Error}>()
)

export const postBlogRequested = createAction(
    "[Blogs] Post Blog Requested",
    props<{payload:{title:string,blogText:string}}>()
)

export const postBlogSucceeded = createAction(
    "[Blogs] Post Blog Succeeded",
    props<{payload:Blog}>()
)

export const postBlogFailed = createAction(
    "[Blogs] Post Blog Failed",
    props<{error:Error}>()
)

export const postThumbnailRequested = createAction(
    '[Blogs] Post Thumbnail Requested',
    props<{payload:{file:File,id:number}}>()
)

export const postThumbnailSucceeded = createAction(
    '[Blogs] Post Thumbnail Succeeded'
)

export const postThumbnailFailed = createAction(
    '[Blogs] Post Thumbnail Failed',
    props<{error:Error}>()
)

export const clearBlogs = createAction('[Blogs] Clear Blogs')