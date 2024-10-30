package com.microsl.loginsignup

class Users{
    var userId: String? = null
    var name: String? = null
    var picture: String? = null
    var email: String? = null
    var semester: String? = null
    var department: String? = null
    var institute: String? = null
    var gender: String? = null

    constructor() {}

    constructor(
        userId: String?,
        name: String?,
        picture: String?,
        email: String?,
        semester: String?,
        department: String?,
        institute: String?,
        gender: String?
    ) {
        this.userId = userId
        this.name = name
        this.picture = picture
        this.email = email
        this.semester = semester
        this.department = department
        this.institute = institute
        this.gender = gender
    }

    // Signup Constructor
    constructor(userId: String?, name: String?, email: String?) {
        this.userId = userId
        this.name = name
        this.email = email
    }

    // Google SignIn Constructor
    constructor(
        userId: String?,
        name: String?,
        picture: String?,
        email: String?
    ) {
        this.userId = userId
        this.name = name
        this.picture = picture
        this.email = email
    }

}