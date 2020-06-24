db.createUser(
    {
        user: "oktana_username",
        pwd: "oktana_password",
        roles: [
            {
                role: "readWrite",
                db: "oktana"
            }
        ]
    }
)