package cl.accenture.githubjavapop.model

class Pull {
    var title: String? = null
    var body: String? = null
    var state: String? = null
    var user: Owner? = null

    constructor(title: String?, body: String?,state: String?, user: Owner?) {
        this.title = title
        this.body = body
        this.state = state
        this.user = user
    }


}