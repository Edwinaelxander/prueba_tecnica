<%-- 
    Document   : index.jsp
    Created on : 7/10/2024, 2:44:37 p. m.
    Author     : edwin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PRUEBA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</head>
<style>
    .container {
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>

<body>
    <div class="container">
        <h1>BIBLIOTECA</h1>
    </div>

    <div class="d-flex">
        <div class="card-body parte01"
            style="margin-left: 10px ;margin-top: 10px;border: 1px solid #F2F4F4  ;margin-right: 10px;">

            <form id="agregarLibro" onsubmit="agregarLibro(event)">
                <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label">Nombre del libro</label>
                    <input type="text" class="form-control" value="" name="txtnombre" id="nombre"
                        aria-describedby="emailHelp">

                </div>

                <input type="submit" class="btn btn-success" value="Agregar">
                <input type="submit" class="btn btn-primary" onclick="buscarLibro(event)" value="Buscar">
            </form>
        </div>

        <div class="card col-sm-6">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ISD</th>
                        <th>LIBRO</th>
                        <th>EDITAR</th>
                        <th>ELIMINAR</th>
                </thead>
                <tbody id="table">
                    
                </tbody>

            </table>
        </div>
    </div>




    <!-- Modal para ediatar libro-->
    <div class="modal fade" id="modalEditar" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
        aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="staticBackdropLabel">Modificar libro</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="editarLibro" onsubmit="editarLibro(event)">
                    <div class="modal-body">


                        <div class="mb-3">
                            <label for="exampleInputEmail1" class="form-label">Nombre del libro</label>
                            <input type="text" class="form-control"  name="txtnombre" id="editarNombre"
                                aria-describedby="emailHelp">
                            <input type="hidden" class="form-control"  name="id" id="editarId" required>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">cerrar</button>
                        <button type="submit" class="btn btn-primary">Actualizar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script src="FuncionesPrueba.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>



</body>

</html>
