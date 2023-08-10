import { User } from "src/app/user/user.model"

export interface Blog{
    id:number
    user:User
    title:string
    blogText:string
    postedAt:Date
    updatedAt:Date

}