package cl.accenture.githubjavapop.modelo

class Pull {
    var title :String? = null
    var body :String?=null
    var user :Owner? =null


    constructor(
        title: String?,
        body :String?,
        user:Owner


    ) {
        this.title = title
        this.body = body
        this.user = user

    }

    constructor() {}
}