document.addEventListener("DOMContentLoaded", function() {
    cargarLibros(); // Cargar la lista de libros al inicio
});

function agregarLibro(event) {
    // Prevenir el comportamiento por defecto del formulario (recargar la página)
    event.preventDefault();
    // Capturar los datos del formulario
    const nombreLibro = document.getElementById('nombre').value;
    // Crear un objeto con los datos
    const libro = {
        nombre: nombreLibro
    };
    $.ajax({
        url: "http://localhost:8080/pruebaTecnica/api/libros/",
        type: "POST", // Método HTTP
        contentType: "application/json", // Tipo de contenido que enviamos
        data: JSON.stringify(libro), // Convertimos el objeto libro a JSON
        success: function (response) {

            alert('Libro agregado con éxito');
            $('#agregarLibro')[0].reset(); // Limpiar el formulario
            cargarLibros(); // Recargar la tabla
        },
        error: function (xhr, status, error) {
            console.error('Error al agregar el libro:', error); // Mostrar errores en consola
            alert('Ocurrió un error al agregar el libro.');
        }
    });
}

// Función para editar un libro
function editarLibro(event) {
    event.preventDefault(); // Prevenir el comportamiento por defecto del formulario (recargar la página)

    // Capturar los datos del formulario
    const idLibro = $('#editarId').val();
    const nombreLibro = $('#editarNombre').val(); // Nombre del libro modificado
console.log(idLibro)
console.log(nombreLibro)
    // Crear un objeto con los datos
    const libro = {
        id: idLibro,
        nombre: nombreLibro
    };
    // Enviar la solicitud AJAX usando jQuery
    $.ajax({
        url: "http://localhost:8080/pruebaTecnica/api/libros/", // URL con el ID del libro
        type: "PUT", // Método HTTP PUT para editar
        contentType: "application/json", // Tipo de contenido que enviamos
        data: JSON.stringify(libro), // Convertir el objeto libro a JSON
        success: function (response) {

            alert('Libro editado con éxito');
            $('#modalEditar').modal('hide'); // Ocultar el modal de edición
            cargarLibros(); // Recargar la tabla
        },
        error: function (xhr, status, error) {
            console.error('Error al editar el libro:', error); // Mostrar errores en consola
            alert('Ocurrió un error al editar el libro.');
        }
    });
}

// Función para buscar un libro por nombre
function buscarLibro(event) {
    event.preventDefault(); // Prevenir el comportamiento por defecto del formulario

    const nombreLibro = $('#nombre').val(); // Obtener el nombre del libro desde el input


    $.ajax({
        url: "http://localhost:8080/pruebaTecnica/api/libros/", // URL con el parámetro de búsqueda
        type: "GET", // Método HTTP GET para buscar
        success: function (response) {

            if (response.length > 0) {

                const tbody = $('table tbody');
                tbody.empty();
                response.forEach(libro => {
                    if (libro.nombre == nombreLibro) {


                        const row = `
                    <tr>
                        <td>${libro.id}</td>
                        <td>${libro.nombre}</td>
                        <td>
                             <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#modalEditar"  onclick="colocarDatos('${libro.nombre}', ${libro.id})">
                                actualizar
                            </button>
                        </td>
                        <td>
                            <button type="button" class="btn btn-danger" onclick="borrarLibro(${libro.id})">
                              eliminar
                            </button>
                        </td>
                    </tr>
                `;
                        tbody.append(row);
                    }
                });
            } else {
                alert('No se encontró ningún libro con ese nombre.');
            }
        },
        error: function (xhr, status, error) {
            console.error('Error al buscar el libro:', error); // Mostrar errores en consola
            alert('Ocurrió un error al buscar el libro.');
        }
    });
}

function cargarLibros() {
    $.ajax({
        url: "http://localhost:8080/pruebaTecnica/api/libros/",
        type: "GET",
        success: function (response) {
            const tbody = $('table tbody');
            tbody.empty();
            response.forEach(libro => {

                const row = `
                    <tr>
                        <td>${libro.id}</td>
                        <td>${libro.nombre}</td>
                        <td>
                             <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#modalEditar"  onclick="colocarDatos('${libro.nombre}', ${libro.id})">
                                actualizar
                            </button>
                        </td>
                        <td>
                            <button type="button" class="btn btn-danger" onclick="borrarLibro(${libro.id})">
                              eliminar
                            </button>
                        </td>
                    </tr>
                `;
                tbody.append(row);
            });
        },
        error: function (xhr, status, error) {
            console.error('Error al cargar los libros:', error);
        }
    });
}


// Función para borrar un libro
function borrarLibro(idLibro) {
    if (confirm('¿Estás seguro de que deseas eliminar este libro?')) {
        const libro = {
            id: idLibro
        };
        // Enviar la solicitud AJAX usando jQuery

        $.ajax({
            url: "http://localhost:8080/pruebaTecnica/api/libros/", // URL con el ID del libro
            contentType: "application/json", // Tipo de contenido que enviamos
            data: JSON.stringify(libro),
            type: "DELETE",
            // Método HTTP DELETE para borrar
            success: function (response) {

                alert('Libro eliminado con éxito');
                cargarLibros(); // Recargar la tabla

            },
            error: function (xhr, status, error) {
                console.error('Error al eliminar el libro:', error); // Mostrar errores en consola
                alert('Ocurrió un error al eliminar el libro.');
            }
        });
    }
}

function colocarDatos(nombre, id) {
    $('#editarNombre').val(nombre);
    $('#editarId').val(id);

}


