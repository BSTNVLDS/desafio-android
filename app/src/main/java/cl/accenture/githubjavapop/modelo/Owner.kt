package cl.accenture.githubjavapop.modelo

class Owner {
    var avatar_url: String? = null
    var login: String? = null

    constructor(avatar_url: String?, login: String?) {
        this.avatar_url = avatar_url
        this.login = login
    }

    constructor() {}
}