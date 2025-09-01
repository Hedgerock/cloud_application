import {AppStructure} from "../components/template/AppStructure.tsx";
import {useGetUserByIdQuery} from "../redux/api/usersApi.ts";

export const StartingPage = () => {
    const { isLoading, data } = useGetUserByIdQuery(1);

    return (
        <AppStructure>
            <h1>Hello world</h1>
            { !isLoading &&
                <div>
                    <h1>{ data?.email }</h1>
                    <div>
                        <h4>id:</h4>
                        <span>{ data?.id }</span>
                    </div>
                    <div>
                        <h4>Total memory:</h4>
                        <span>{ data?.diskSpace }</span>
                    </div>
                    <div>
                        <h4>Used memory:</h4>
                        <span>{ data?.usedSpace }</span>
                    </div>
                    <div>
                        <h4>Files</h4>
                        { data?.files?.length &&
                            <ul>
                                {data.files.map(el => {
                                        return (
                                            <li key={el.id}>{ el.name }</li>
                                        )
                                    })
                                }
                            </ul>
                        }
                    </div>
                    <div>
                        <h4>id:</h4>
                        <span>{ data?.id }</span>
                    </div>
                </div>
            }
        </AppStructure>
    )
}