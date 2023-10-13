package mx.tec.navegacionyconstraint

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import mx.tec.navegacionyconstraint.ui.theme.NavegacionYConstraintTheme



// todays topic - navegación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavegacionYConstraintTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navegacion(this)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

// composables para navegación

// 1 interfaz principal

// 2 secundarias
@Composable
fun KittenInterface(
    regresar : () -> Unit,
    nombre: String? = "",
    peso: Float? = 1.0f
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AsyncImage(
            model = "https://www.warrenphotographic.co.uk/photography/sqrs/01149.jpg",
            contentDescription = "a kitten"
        )

        Button(
            onClick = regresar
        ){
            Text("regresar")
        }

        Text("nombre: $nombre");
        Text("peso: $peso")
    }
}

@Composable
fun PuppyInterface(
    regresar: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AsyncImage(
            model = "https://www.warrenphotographic.co.uk/photography/sqrs/01145.jpg",
            contentDescription = "perrito."
        )
        Button(
            onClick = regresar
        ){
            Text("regresar")
        }
    }
}

@Composable
fun MainMenu(
    kittenInterfaceButtonLogic : () -> Unit,
    puppyInterfaceButtonLogic : () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            onClick = kittenInterfaceButtonLogic
        ) {
            Text("ir a interfaz de gatito")
        }
        Button(
            onClick = puppyInterfaceButtonLogic
        ) {
            Text("ir a interfaz de perrito")
        }
    }
}

@Composable
fun Navegacion(activity: Activity) {
    // este va a ser el administrador principal de mis vistas
    // necesitamos declarar un controller primero
    // controller es el objeto que tiene la lógica para el intercambio
    // de vistas y datos
    val navController = rememberNavController()

    // host - estructura donde viven las interfaces navegables
    NavHost(
        navController = navController,
        startDestination = "mainMenu"
    ){

        // dentro del navhost vamos a declarar varios composables que podemos navegar
        // utilizando la macro composable
        composable("mainMenu"){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                MainMenu(
                    kittenInterfaceButtonLogic = {
                        navController.navigate("kittenInterface/Michi/3.5")
                    },
                    puppyInterfaceButtonLogic = {
                        navController.navigate("puppyInterface")
                    }
                )

                // obtener valores desde el save state handle
                // ?. - safe call - si el objeto es null no ejecuta línea
                val result = navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.getLiveData<String>("nombreDePerrito")
                    ?.observeAsState()

                // let - scope function
                // espacio donde ejecutamos código en un contexto particular
                // let - en este caso el hecho que una variable / objeto estén definidos
                result?.value?.let {nombre ->
                    Text("el perrito se llama: $nombre")

                    // si desean pueden borrar el valor para limpiar
                    navController
                        .currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<String>("nombreDePerrito")
                }

                Button(
                    onClick = {
                        val intent = Intent(activity, ConstraintLayoutActivity::class.java)
                        activity.startActivity(intent)
                    }
                ){
                    Text("Constraint Layout Example")
                }
            }
        }
        composable(
            "kittenInterface/{nombre}/{peso}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType},
                navArgument("peso") { type = NavType.FloatType}
            )
        ) { backStackEntry ->
            KittenInterface(
                regresar = {
                    navController.popBackStack()
                },
                nombre = backStackEntry.arguments?.getString("nombre"),
                peso = backStackEntry.arguments?.getFloat("peso")

            )
        }
        composable("puppyInterface") {
            PuppyInterface (
                regresar = {
                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("nombreDePerrito", "Firulais")
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NavegacionYConstraintTheme {
        Greeting("Android")
    }
}