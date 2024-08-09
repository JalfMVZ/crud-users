User Management API
Esta API, construida con Spring Boot, gestiona usuarios con operaciones CRUD (GET, Delete,Update,POST) y usa una base de datos H2 en memoria

Endpoints:

URL: http://localhost:8000/api/users
Método: GET
Descripción: Lista todos los usuarios.
Obtener Usuario por ID

URL: http://localhost:8000/api/users/{id}
Método: GET
Descripción: Obtiene un usuario por su ID.
Crear un Nuevo Usuario

URL: http://localhost:8000/api/users
Método: POST
Descripción: Crea un nuevo usuario.
Actualizar un Usuario

URL: http://localhost:8000/api/users/{id}
Método: PUT
Descripción: Actualiza un usuario existente.
Eliminar un Usuario

URL: http://localhost:8000/api/users/{id}
Método: DELETE
Descripción: Elimina un usuario por su ID.