import type {FC} from "react";
import type {ICurrentEntity} from "../../models/ICurrentEntity.ts";
import type {IUser} from "../../models/IUser.ts";

export const CurrentUser: FC<ICurrentEntity<IUser>> = ({ entity:data }) => {


    return (
        <>
            <h1>Hello world</h1>

            <div>
                <h1>{ data.email }</h1>
                <div>
                    <h4>id:</h4>
                    <span>{ data.id }</span>
                </div>
                <div>
                    <h4>Total memory:</h4>
                    <span>{ data.diskSpace }</span>
                </div>
                <div>
                    <h4>Used memory:</h4>
                    <span>{ data.usedSpace }</span>
                </div>
                <div>
                    <h4>Files</h4>
                    { data.files.length &&
                        <ul>
                            {data.files.map(el => {
                                return <li key={el.id}>{ el.name }</li>
                            })}
                        </ul>
                    }
                </div>
                <div>
                    <h4>id:</h4>
                    <span>{ data.id }</span>
                </div>
            </div>
        </>
    )
}