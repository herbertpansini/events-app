package br.com.southsystem.eventsapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.southsystem.eventsapp.model.dao.EventDao
import br.com.southsystem.eventsapp.model.entity.Event

private const val NOME_BANCO_DE_DADOS = "event.db"

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, NOME_BANCO_DE_DADOS)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        ioThread {
                            getInstance(context).eventDao().deleteAll()
                            getInstance(context).eventDao().salva(PREPOPULATE_DATA)
                        }
                    }
                })
                .build()

        val PREPOPULATE_DATA = listOf(
            Event("1",
                "Feira de adoção de animais na Redenção",
                1534784400,
                "O Patas Dadas estará na Redenção, nesse domingo, com cães para adoção e produtos à venda!\n\nNa ocasião, teremos bottons, bloquinhos e camisetas!\n\nTraga seu Pet, os amigos e o chima, e venha aproveitar esse dia de sol com a gente e com alguns de nossos peludinhos - que estarão prontinhos para ganhar o ♥ de um humano bem legal pra chamar de seu. \n\nAceitaremos todos os tipos de doação:\n- guias e coleiras em bom estado\n- ração (as que mais precisamos no momento são sênior e filhote)\n- roupinhas \n- cobertas \n- remédios dentro do prazo de validade",
                "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png",
                29.99,
                "-51.2146267",
                "-30.0392981"),
            Event("2",
                "Doação de roupas",
                1534784400,
                "Vamos ajudar !!\\n\\nSe você tem na sua casa roupas que estão em bom estado de uso e não sabemos que fazer, coloque aqui na nossa página sua cidade e sua doação, com certeza poderá ajudar outras pessoas que estão passando por problemas econômicos no momento!!\\n\\nAjudar não faz mal a ninguém!!!\\n",
                "http://fm103.com.br/wp-content/uploads/2017/07/campanha-do-agasalho-balneario-camboriu-2016.jpg",
                59.99,
                "-51.2148497",
                "-30.037878"),
            Event("3",
                "Feira de Troca de Livros",
                1534784400,
                "Atenção! Para nosso brique ser o mais organizado possível, leia as regras e cumpra-as:\\n* Ao publicar seus livros, evite criar álbuns (não há necessidade de remetê-los a nenhum álbum);\\n* A publicação deverá conter o valor desejado;\\n* É preferível publicar uma foto do livro em questão a fim de mostrar o estado em que se encontra;\\n* Respeite a ordem da fila;\\n* Horário e local de encontro devem ser combinados inbox;\\n* Caso não possa comparecer, avise seu comprador/vendedor previamente;\\n* Caso seu comprador desista, comente o post com \\\"disponível\\\";\\n* Não se esqueça de apagar a publicação se o livro já foi vendido, ou ao menos comente \\\"vendido\\\" para que as administradoras possam apagá-la;\\n* Evite discussões e respeite o próximo;\\n",
                "http://www.fernaogaivota.com.br/documents/10179/1665610/feira-troca-de-livros.jpg",
                19.99,
                "-51.2148497",
                "-30.037878"),
            Event("4",
                "Feira de Produtos Naturais e Orgânicos",
                1534784400,
                "\"Toda quarta-feira, das 17h às 22h, encontre a feira mais charmosa de produtos frescos, naturais e orgânicos no estacionamento do Shopping. Sintonizado com a tendência crescente de busca pela alimentação saudável, consumo consciente e qualidade de vida. \\n\\nAs barracas terão grande variedade de produtos, como o shiitake cultivado em Ibiporã há mais de 20 anos, um sucesso na mesa dos que não abrem mão do saudável cogumelo na dieta. Ou os laticínios orgânicos da Estância Baobá, famosos pelo qualidade e modo de fabricação sustentável na vizinha Jaguapitã. Também estarão na feira as conhecidas compotas e patês tradicionais da Pousada Marabú, de Rolândia.\\n\\nA feira do Catuaí é uma nova opção de compras de produtos que não são facilmente encontrados no varejo tradicional, além de ótima pedida para o descanso de final de tarde em família e entre amigos. E com o diferencial de ser noturna, facilitando a vida dos consumidores que poderão sair do trabalho e ir direto para a “Vila Verde”, onde será possível degustar delícias saudáveis nos bistrôs, ouvir música ao vivo, levar as crianças para a diversão em uma estação de brinquedos e relaxar ao ar livre.\\n\\nEXPOSITORES DA VILA VERDE CATUAÍ\\n\\nCraft Hamburgueria\\nNido Pastíficio\\nSabor e Saúde\\nTerra Planta\\nEmpório da Papinha\\nEmpório Sabor da Serra\\nBoleria Dom Leonardi\\nCoisas que te ajudam a viver\\nPatês da Marisa\\nMarabú\\nBaobá\\nAkko\\nCervejaria Amadeus\\n12 Tribos\\nParr Kitchen\\nHorta Fazenda São Virgílio\\nHorta Chácara Santo Antonio\\nSur Empanadas\\nFit & Sweet\\nSK e T Cogumelos\\nDos Quintana\\n\\nLocal: Estacionamento (entrada principal do Catuaí Shopping Londrina)\\n\\n\\nAcesso à Feira gratuito.\"",
                "https://i2.wp.com/assentopublico.com.br/wp-content/uploads/2017/07/Feira-de-alimentos-org%C3%A2nicos-naturais-e-artesanais-ganha-um-novo-espa%C3%A7o-em-Ribeir%C3%A3o.jpg",
                19.0,
                "-51.2148497",
                "-30.037878")
        )
    }
}