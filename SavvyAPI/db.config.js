
/**
 * @class
 * this class serves as setup to connect to mysql and create mysql pools.
 */
const mysql = require('mysql')

const db = mysql.createPool({
    connectionLimit: 10,
    host: 'localhost',
    user: 'root',
    password: 'BzX3J#RgczvFxK3$',
    database: 'users_database'

})


module.exports = db

