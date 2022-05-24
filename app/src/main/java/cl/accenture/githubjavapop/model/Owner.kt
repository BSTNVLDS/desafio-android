package cl.accenture.githubjavapop.model

class Owner {
    var avatar_url: String? = null
    var login: String? = null
    var name: String? = null

    constructor() {}

    constructor(avatar_url: String?, login: String?) {
        this.avatar_url = avatar_url
        this.login = login

    }
}