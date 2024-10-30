package com.microsl.loginsignup

class Users {
    var userId: String? = null
    var name: String? = null
    var profile: String? = null
    var email: String? = null
    var password: String? = null
    var semester: String? = null
    var department: String? = null
    var institute: String? = null
    var gender: String? = null

    constructor() {}

    constructor(
        userId: String?,
        name: String?,
        profile: String?,
        email: String?,
        password: String?,
        semester: String?,
        department: String?,
        institute: String?,
        gender: String?
    ) {
        this.userId = userId
        this.name = name
        this.profile = profile
        this.email = email
        this.password = password
        this.semester = semester
        this.department = department
        this.institute = institute
        this.gender = gender
    }

    // Signup Constructor
    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

}