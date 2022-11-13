import {Layout, Menu} from "antd";
import {CalculatorOutlined, TableOutlined} from "@ant-design/icons";
import {Link} from "react-router-dom";

const {Header} = Layout

export default function MyHeader({selectedMenuItem}){
    const items = [
        {
            icon: <TableOutlined/>,
            label: (
                <Link to={"/catalog"}>
                    Catalog
                </Link>
            ),
            key: "catalog"
        },
        {
            icon: <CalculatorOutlined/>,
            label: (
                <Link to={"/agency"}>
                    Agency
                </Link>
            ),
            key: "agency"
        }
    ];

    return (
        <>
            <Header>
                <Menu
                    theme={"dark"}
                    mode={"horizontal"}
                    items={items}
                    defaultSelectedKeys={[selectedMenuItem]}
                />
            </Header>
        </>
    )
}