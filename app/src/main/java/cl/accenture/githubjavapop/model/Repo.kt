package cl.accenture.githubjavapop.model

class Repo {
    var name: String? = null
    var description: String? = null
    var forks: String? = null
    var watchers: String? = null
    var owner: Owner? = null

    constructor(
        name: String?,
        description: String?,
        forks: String?,
        watchers: String?,
        owner: Owner?
    ) {
        this.name = name
        this.description = description
        this.forks = forks
        this.watchers = watchers
        this.owner = owner
    }

    constructor() {}
}